package com.moyu.test.store.data;

import com.moyu.test.exception.SqlExecutionException;
import com.moyu.test.util.DataUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/5/11
 * 固定大小为1024字节的数据块，一个数据块里面包含多行数据
 */
public class DataChunk {

    public static final int DATA_CHUNK_LEN = 4096;

    /**
     * 已使用字节长度
     */
    private int usedByteLen;
    /**
     * 数据块下标
     */
    private int chunkIndex;
    /**
     * 块内数据总行数
     */
    private int rowNum;
    /**
     * 整块的开始位置
     */
    private long startPos;
    /**
     * 数据行的开始位置
     */
    private long rowStartPos;
    /**
     * 下一个数据行开始位置
     */
    private long nextRowStartPos;
    /**
     * 行数据
     */
    private List<RowData> dataRowList;

    public DataChunk(int chunkIndex, long startPos) {
        initDataChunk(chunkIndex, startPos);
    }

    private void initDataChunk(int chunkIndex, long startPos) {
        // 4(usedByteLen) + 4(chunkIndex) + 4(rowNum) + 8(startPos) + 8(dataStartPos) + 8(nextRowStartPos) = 36
        this.usedByteLen = 36;
        this.chunkIndex = chunkIndex;
        this.rowNum = 0;
        this.startPos = startPos;
        this.rowStartPos = this.usedByteLen;
        this.nextRowStartPos = this.rowStartPos;
        this.dataRowList = new ArrayList<>();
    }

    public DataChunk(ByteBuffer byteBuffer) {
        this.usedByteLen = DataUtils.readInt(byteBuffer);
        this.chunkIndex = DataUtils.readInt(byteBuffer);
        this.rowNum = DataUtils.readInt(byteBuffer);
        this.startPos = DataUtils.readLong(byteBuffer);
        this.rowStartPos = DataUtils.readLong(byteBuffer);
        this.nextRowStartPos = DataUtils.readLong(byteBuffer);

        this.dataRowList = new ArrayList<>();
        for (int i = 0; i < this.rowNum; i++) {
            this.dataRowList.add(new RowData(byteBuffer));
        }
    }


    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DATA_CHUNK_LEN);
        DataUtils.writeInt(byteBuffer, this.usedByteLen);
        DataUtils.writeInt(byteBuffer, this.chunkIndex);
        DataUtils.writeInt(byteBuffer, this.rowNum);
        DataUtils.writeLong(byteBuffer, this.startPos);
        DataUtils.writeLong(byteBuffer, this.rowStartPos);
        DataUtils.writeLong(byteBuffer, this.nextRowStartPos);

        if (this.dataRowList != null && this.dataRowList.size() > 0) {
            for (int i = 0; i < this.dataRowList.size(); i++) {
                byteBuffer.put(dataRowList.get(i).getByteBuff());
            }
        }
        byteBuffer.rewind();

        return byteBuffer;
    }


    /**
     * 数据块剩余字节数
     * @return
     */
    public int remaining() {
        return DATA_CHUNK_LEN - usedByteLen;
    }


    public void addRow(RowData dataRow) {
        this.rowNum++;
        this.usedByteLen += dataRow.getTotalByteLen();
        this.nextRowStartPos = this.nextRowStartPos + dataRow.getTotalByteLen();
        this.dataRowList.add(dataRow);
    }

    public void updateRow(int index, RowData newRow) {
        if(index >= dataRowList.size()) {
            throw new SqlExecutionException("超出下标,size: " + dataRowList.size() + ",index:" + index);
        }
        RowData oldRow = dataRowList.get(index);
        long needLen = newRow.getTotalByteLen() - oldRow.getTotalByteLen();
        this.usedByteLen += needLen;
        this.nextRowStartPos = needLen;
        dataRowList.set(index, newRow);
    }


    public void removeRow(int index) {
        if(index >= dataRowList.size()) {
           throw new SqlExecutionException("超出下标,size: " + dataRowList.size() + ",index:" + index);
        }
        RowData rowData = dataRowList.get(index);
        this.rowNum--;
        this.usedByteLen = this.usedByteLen - (int) rowData.getTotalByteLen();
        this.nextRowStartPos = this.nextRowStartPos - (int) rowData.getTotalByteLen();
        this.dataRowList.remove(index);
    }

    public void markRowIsDeleted(int index) {
        if(index >= dataRowList.size()) {
            throw new SqlExecutionException("超出下标,size: " + dataRowList.size() + ",index:" + index);
        }
        RowData rowData = dataRowList.get(index);
        rowData.setIsDeleted((byte) 1);
    }

    public void clear() {
        initDataChunk(this.chunkIndex, this.getStartPos());
    }


    public static int getDataChunkLen() {
        return DATA_CHUNK_LEN;
    }

    public int getUsedByteLen() {
        return usedByteLen;
    }

    public void setUsedByteLen(int usedByteLen) {
        this.usedByteLen = usedByteLen;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getRowStartPos() {
        return rowStartPos;
    }

    public void setRowStartPos(long rowStartPos) {
        this.rowStartPos = rowStartPos;
    }

    public List<RowData> getDataRowList() {
        return dataRowList;
    }

    public void setDataRowList(List<RowData> dataRowList) {
        this.dataRowList = dataRowList;
    }

    public long getNextRowStartPos() {
        return nextRowStartPos;
    }

    public void setNextRowStartPos(long nextRowStartPos) {
        this.nextRowStartPos = nextRowStartPos;
    }

    @Override
    public String toString() {
        return "DataChunk{" +
                "totalByteLen=" + usedByteLen +
                ", chunkIndex=" + chunkIndex +
                ", rowNum=" + rowNum +
                ", startPos=" + startPos +
                ", rowStartPos=" + rowStartPos +
                ", nextRowStartPos=" + nextRowStartPos +
                ", dataRowList=" + dataRowList +
                '}';
    }
}
