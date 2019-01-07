import React from 'react'

export default () => {

        return (
            <nav className="navbar navbar-default">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <a className="navbar-brand" href="/routes">Маршруты</a>
                        <a className="navbar-brand" href="/flights">Рейсы</a>
                        <a className="navbar-brand" href="/tickets">Билеты</a>
                    </div>

                    <div className="collapse navbar-collapse">
                        <ul className="nav navbar-nav navbar-right">
                            <li><a href="/login">Войти</a></li>
                            <li><a href="/signup">Зарегистрироваться</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
        );
}