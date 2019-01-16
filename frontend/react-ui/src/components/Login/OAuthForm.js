import React from 'react'
import {Button} from 'react-bootstrap'
import "../../styles/Greetings.css"
import {checkService} from '../../actions/OAuthActions';
import { login } from '../../actions/AuthActions';
import {connect} from "react-redux";
import {addFlashMessage} from "../../actions/FlashMessages";
import PropTypes from "prop-types";

class OAuthForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            serviceName: '',
            redirectUri: ''
        };

        this.sendAccess = this.sendAccess.bind(this);
    }

    componentDidMount() {
        this.setState({
            redirectUri: this.props.location.query.redirectUri
        });
        this.props.checkService(this.props.location.query.serviceUuid)
            .then(res => {
                this.setState({
                    serviceName: res.data.name
                });
            })
            .catch(err => {
                console.log(err);
            });
    }

    sendAccess() {
        const data = {
            'identifier': localStorage.getItem('identifier'),
            'password': sessionStorage.getItem('password'),
            'redirectUri': this.state.redirectUri
        };
        this.props.login(data)
            .then(() => {
                localStorage.removeItem('url');
                window.close();
            })
            .catch(err => {
                console.log(err);
                alert('Error');
            })
    }

    render() {
        return (
            <div className="jumbotron">
                <h2>Сервис <strong>{this.state.serviceName}</strong> запрашивает доступ к Вашим ресурсам</h2>
                <hr/>
                <Button className="continue-button" bsStyle="primary" onClick={this.sendAccess}>Разрешить</Button>
            </div>
        );
    }
}

OAuthForm.propTypes = {
    login: PropTypes.func.isRequired,
    addFlashMessage: PropTypes.func.isRequired,
    checkService: PropTypes.func.isRequired
};

OAuthForm.contextTypes = {
    router: PropTypes.object.isRequired
};

export default connect(null, { login, addFlashMessage, checkService })(OAuthForm);