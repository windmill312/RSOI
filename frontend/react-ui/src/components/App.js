import React from 'react'
import '../styles/App.css'
import "react-datepicker/dist/react-datepicker.css";
import NavigationBar from './common/NavigationBar';
import FlashMessagesList from '../components/flash/FlashMessagesList';
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            activeTab: '0',
            disabled: false,
            userUuid: '',
            userName: '',
            userInfo: []
        }

    }

    render() {
        return (
            <div className="container">
                <NavigationBar />
                <FlashMessagesList />
                {this.props.children}
            </div>
        )
    }
}

export default App;