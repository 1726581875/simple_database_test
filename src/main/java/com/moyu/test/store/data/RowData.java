package com.moyu.test.store.data;

import com.moyu.test.store.WriteBuffer;
import com.moyu.test.store.data.cursor.RowEntity;
import com.moyu.test.store.metadata.obj.Column;
import com.moyu.test.store.type.DataType;
import com.moyu.test.store.type.ColumnTypeFactory;
import com.moyu.test.util.DataUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/5/11
 * 行数据，每一行数据(元组)的所有字节
 */
public class RowData {

    private long totalByteLen;

    private long startPos;

    private int rowByteLen;
    /**
     * 数据行id
     */
    private long rowId;

    /**
     * 数据是否已删除
     * 0 否
     * 1 是
     */
    private byte isDeleted;

    /**
     * 包含该行所有字段的数据
     * 存储格式是按列字段顺序和值，byte数组是[字段a的值描述 + 字段a的值描述 + 字段..的描述]
     *
     *  例1: 存储一个字段a，类型为int，值为3。其描述如下
     *   1字节标记位 [000000001]  + 4字节int [00000000 00000000 00000000 00000011]
     *
     *  例2: 存储字段b，类型为varchar，值为'A'。其描述如下
     *  1字节标记位 [000000001]  + 4字节int字符长度 [00000000 00000000 00000000 00000001]  + 字符内容‘A’对应的二进制字节 [1000001]
     *
     *  标记位是标记该值是否为空,0表示值为空、1表示值非空
     */
    private byte[] row;


    public RowData(long startPos, byte[] row) {
        this.startPos = startPos;
        this.rowByteLen = row.length;
        this.row = row;
        this.totalByteLen = 29 + row.length;
    }

    public RowData(long startPos, byte[] row, long rowId) {
        this.startPos = startPos;
        this.rowByteLen = row.length;
        this.row = row;
        this.rowId = rowId;
        this.isDeleted = 0;
        this.totalByteLen = 29 + row.length;
    }

    public RowData(ByteBuffer byteBuffer) {
        this.totalByteLen = DataUtils.readLong(byteBuffer);
        this.startPos = DataUtils.readLong(byteBuffer);
        this.rowByteLen = DataUtils.readInt(byteBuffer);
        this.rowId = DataUtils.readLong(byteBuffer);
        this.isDeleted = byteBuffer.get();
        byte[] row = new byte[rowByteLen];
        byteBuffer.get(row);
        this.row = row;
    }


    public ByteBuffer getByteBuff() {
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) totalByteLen);
        DataUtils.writeLong(byteBuffer, this.totalByteLen);
        DataUtils.writeLong(byteBuffer, this.startPos);
        DataUtils.writeInt(byteBuffer, this.rowByteLen);
        DataUtils.writeLong(byteBuffer, this.rowId);
        byteBuffer.put(isDeleted);
        byteBuffer.put(this.row);
        byteBuffer.rewind();
        return byteBuffer;
    }


    public static byte[] toRowByteData(List<Column> columnList) {
        Column[] columns = columnList.toArray(new Column[0]);
        return toRowByteData(columns);
    }

    public static byte[] toRowByteData(Column[] columns) {
        WriteBuffer writeBuffer = new WriteBuffer(16);
        for (Column column : columns) {
            DataType columnType = ColumnTypeFactory.getColumnType(column.getColumnType());
            columnType.write(writeBuffer, column.getValue());
        }
        writeBuffer.getBuffer().flip();
        byte[] result = new byte[writeBuffer.limit()];
        writeBuffer.get(result);
        return result;
    }

    public List<Column> getColumnList(List<Column> columnList) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(row);
        for (Column column : columnList) {
            DataType columnType = ColumnTypeFactory.getColumnType(column.getColumnType());
            Object value = columnType.read(byteBuffer);
            column.setValue(value);
        }
        return columnList;
    }

    public Column[] getColumnData(Column[] columns) {
        Column[] resultColumns = new Column[columns.length];
        ByteBuffer byteBuffer = ByteBuffer.wrap(row);
        for (int i = 0; i < columns.length; i++) {
            Column column = columns[i];
            DataType columnType = ColumnTypeFactory.getColumnType(column.getColumnType());
            Object value = columnType.read(byteBuffer);

            Column valueColumn = column.createNullValueColumn();
            valueColumn.setValue(value);
            resultColumns[i] = valueColumn;
        }
        return resultColumns;
    }

    public RowEntity getRowEntity(Column[] columns) {
        Column[] resultColumns = getColumnData(columns);
        RowEntity rowEntity = new RowEntity(resultColumns);
        rowEntity.setDeleted(this.isDeleted == (byte) 1);
        return rowEntity;
    }


    public long getTotalByteLen() {
        return totalByteLen;
    }

    public void setTotalByteLen(long totalByteLen) {
        this.totalByteLen = totalByteLen;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public int getRowByteLen() {
        return rowByteLen;
    }

    public void setRowByteLen(int rowByteLen) {
        this.rowByteLen = rowByteLen;
    }

    public byte[] getRow() {
        return row;
    }

    public void setRow(byte[] row) {
        this.row = row;
    }

    public long getRowId() {
        return rowId;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return "DataRow{" +
                "totalByteLen=" + totalByteLen +
                ", startPos=" + startPos +
                ", rowByteLen=" + rowByteLen +
                ", row=" + Arrays.toString(row) +
                '}';
    }
}
