package com.moyu.test.command.dml.sql;

import com.moyu.test.command.dml.condition.ConditionTree;
import com.moyu.test.store.metadata.obj.Column;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2023/6/7
 */
public class TableFilter {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表别名
     */
    private String alias;
    /**
     * 当前表所有字段信息
     */
    private Column[] allColumns;
    /**
     * 查询条件
     */
    private ConditionTree tableCondition;
    /**
     * 连接类型
     * LEFT 左连接
     * INNER 内连接
     */
    private String joinInType;
    /**
     * 连接条件
     */
    private ConditionTree joinCondition;
    /**
     * 连接的表
     */
    private List<TableFilter> joinTables;
    /**
     * 子查询
     */
    private TableFilter subQuery;


    public TableFilter(String tableName, Column[] allColumns, ConditionTree tableCondition) {
        this.tableName = tableName;
        this.allColumns = allColumns;
        this.tableCondition = tableCondition;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ConditionTree getTableCondition() {
        return tableCondition;
    }

    public void setTableCondition(ConditionTree tableCondition) {
        this.tableCondition = tableCondition;
    }

    public String getJoinInType() {
        return joinInType;
    }

    public void setJoinInType(String joinInType) {
        this.joinInType = joinInType;
    }

    public List<TableFilter> getJoinTables() {
        return joinTables;
    }

    public void setJoinTables(List<TableFilter> joinTables) {
        this.joinTables = joinTables;
    }

    public ConditionTree getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(ConditionTree joinCondition) {
        this.joinCondition = joinCondition;
    }

    public Column[] getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(Column[] allColumns) {
        this.allColumns = allColumns;
    }

    public TableFilter getSubQuery() {
        return subQuery;
    }

    public void setSubQuery(TableFilter subQuery) {
        this.subQuery = subQuery;
    }
}