package com.moyu.test.store.data.cursor;

import com.moyu.test.exception.SqlIllegalException;
import com.moyu.test.store.metadata.obj.Column;

import java.util.Arrays;

/**
 * @author xiaomingzhang
 * @date 2023/6/6
 */
public class RowEntity {

    private boolean isDeleted;

    private Column[] columns;

    public RowEntity(Column[] columns) {
        this.columns = columns;
    }

    public RowEntity(Column[] columns, String tableAlias) {

        Column[] arr = new Column[columns.length];
        for (int i = 0; i < columns.length; i++) {
            arr[i] = columns[i].copy();
            arr[i].setTableAlias(tableAlias);
        }
        this.columns = arr;
    }

    public Column[] getColumns() {
        return columns;
    }

    public Column getColumn(String columnName, String tableAlias) {
        for (Column c : columns) {
            if((tableAlias == null || tableAlias.equals(c.getTableAlias()))
                    && c.getColumnName().equals(columnName)) {
                return c;
            }
        }

        String tbAlias = tableAlias == null ? "" : tableAlias + ".";
        throw new SqlIllegalException("字段" + tbAlias + columnName + "不存在");
    }

    public RowEntity setTableAlias(String tableAlias){
        for (int i = 0; i < columns.length; i++) {
            columns[i].setTableAlias(tableAlias);
        }
        return this;
    }


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public static RowEntity mergeRow(RowEntity leftRow, RowEntity rightRow) {
        Column[] columns = Column.mergeColumns(leftRow.getColumns(), rightRow.getColumns());
        return new RowEntity(columns);
    }

    @Override
    public String toString() {
        return "RowEntity{" +
                "columns=" + Arrays.toString(columns) +
                '}';
    }
}
