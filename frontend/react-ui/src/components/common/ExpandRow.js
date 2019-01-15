import React from 'react';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import PropTypes from "prop-types";

class BSTable extends React.Component {
    render() {
        if (this.props.data) {
            return (
                <BootstrapTable data={ this.props.data }>
                    <TableHeaderColumn dataField='uid' isKey={ true }>Уникальный номер билета</TableHeaderColumn>
                    <TableHeaderColumn dataField='classType'>Класс</TableHeaderColumn>
                </BootstrapTable>);
        }
    }
}

export default class ExpandRow extends React.Component {

    isExpandableRow(row) {
        if (row.tickets)
            return true;
        else
            return false;
    }

    expandComponent(row) {
        return (
            <BSTable data={ row.tickets } />
        );
    }

    render() {
        const options = {
            expandRowBgColor: 'rgb(242, 255, 163)'
        };
        return (
            <BootstrapTable data={ this.props.data }
                            options={ options }
                            expandableRow={ this.isExpandableRow }
                            expandComponent={ this.expandComponent }
                            search>
                <TableHeaderColumn dataField='uid' isKey={ true }>Уникальный номер рейса</TableHeaderColumn>
                <TableHeaderColumn dataField='dtFlight'>Дата рейса</TableHeaderColumn>
                <TableHeaderColumn dataField='nnTickets'>Количество купленных билетов</TableHeaderColumn>
                <TableHeaderColumn dataField='maxTickets'>Общее количество билетов</TableHeaderColumn>
            </BootstrapTable>
        );
    }
}

ExpandRow.propTypes ={
    data: PropTypes.array.isRequired
};