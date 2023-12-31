import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Welcome to Get Inventory!</h1>
        <p className="lead">With this service you can book an inventory.</p>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                sign in
              </Link>
              , you can try the default accounts:
              <br />
              - Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;) <br />
              - Kent Kotlin sample tester (login=&quot;kentkotlin&quot; and password=&quot;1234&quot;) <br />
              - Alice Java sample tester (login=&quot;alicejava&quot; and password=&quot;1234&quot;) <br />.
            </Alert>

            <Alert color="warning">
              You don&apos;t have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>
          </div>
        )}

        <p>
          Please review <Link to="/inventory">available inventory</Link> and make a <Link to="/reservation">reservation here</Link>.
        </p>
      </Col>
    </Row>
  );
};

export default Home;
