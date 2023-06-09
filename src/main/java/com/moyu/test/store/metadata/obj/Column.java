package com.moyu.test.store.metadata.obj;

/**
 * @author xiaomingzhang
 * @date 2023/5/9
 */
public class Column {

    private String columnName;

    private String alias;

    private String tableAlias;

    private byte columnType;

    private int columnIndex;

    private int columnLength;

    private byte isPrimaryKey;

    private Object value;


    public Column(String columnName, byte columnType, int columnIndex, int columnLength) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnIndex = columnIndex;
        this.columnLength = columnLength;
        this.isPrimaryKey = 0;
    }


    public Column createNullValueColumn() {
        Column column = new Column(columnName, columnType, columnIndex, columnLength);
        column.setIsPrimaryKey(isPrimaryKey);
        column.setTableAlias(tableAlias);
        column.setAlias(alias);
        column.setValue(null);
        return column;
    }

    public Column copy() {
        Column column = new Column(columnName, columnType, columnIndex, columnLength);
        column.setIsPrimaryKey(isPrimaryKey);
        column.setTableAlias(tableAlias);
        column.setAlias(alias);
        column.setValue(value);
        return column;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public byte getColumnType() {
        return columnType;
    }

    public void setColumnType(byte columnType) {
        this.columnType = columnType;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(int columnLength) {
        this.columnLength = columnLength;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public byte getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(byte isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    @Override
    public boolean equals(Object o) {
        Column c = (Column) o;
        return value != null && value.equals(c.getValue());
    }

    @Override
    public int hashCode() {
        return (value != null ? value.hashCode() : 0);
    }


    public static Column[] mergeColumns(Column[] mainColumns, Column[] joinColumns) {
        int l = mainColumns.length + joinColumns.length;
        Column[] allColumns = new Column[l];
        for (int i = 0; i < l; i++) {
            if (i < mainColumns.length) {
                allColumns[i] = mainColumns[i];
            } else {
                allColumns[i] = joinColumns[i - mainColumns.length];
            }
        }
        return allColumns;
    }

    public static void setColumnTableAlias(Column[] columns, String tableAlias) {
        for (Column c : columns) {
            c.setTableAlias(tableAlias);
        }
    }


    public String getTableAliasColumnName() {
        String tableAlias = this.tableAlias == null ? "" : this.tableAlias + ".";
        return tableAlias + columnName;
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", alias='" + alias + '\'' +
                ", tableAlias='" + tableAlias + '\'' +
                ", columnType=" + columnType +
                ", columnIndex=" + columnIndex +
                ", columnLength=" + columnLength +
                ", isPrimaryKey=" + isPrimaryKey +
                ", value=" + value +
                '}';
    }
}
