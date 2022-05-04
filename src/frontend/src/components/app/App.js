import {lazy, Suspense, useEffect, useState} from 'react'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import AuthService from '../../services/authService';
import LogIn from '../logIn/LogIn';
import Registration from '../logIn/Registration';
import FirstPage from '../../pages/firstPage/FirstPage';
import Spinner from '../spinner/Spinner';
import './App.css';

const MainPage = lazy(() => import("../../pages/mainPage/mainPage"));

const App = () => {
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if(user){
      setCurrentUser(user);
    }
  }, []);


  return (

    <div>
      <Router>
      <div className="App">
        <Suspense fallback={<Spinner/>}>
          <Switch>
            <Route exact path="/">
              <FirstPage/>
            </Route>
            <Route exact path="/home">
              <MainPage/>
            </Route>
            <Route exact path="/login">
              <LogIn/>
            </Route>
            <Route exact path="/registration">
              <Registration/>
            </Route>
          </Switch>
        </Suspense>
      </div>
    </Router>
    </div>
  );
}

export default App;
