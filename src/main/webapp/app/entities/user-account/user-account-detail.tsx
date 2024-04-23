import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-account.reducer';

export const UserAccountDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAccountEntity = useAppSelector(state => state.userAccount.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAccountDetailsHeading">User Account</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{userAccountEntity.id}</dd>
          <dt>
            <span id="loginName">Login Name</span>
          </dt>
          <dd>{userAccountEntity.loginName}</dd>
          <dt>
            <span id="password">Password</span>
          </dt>
          <dd>{userAccountEntity.password}</dd>
          <dt>
            <span id="streetAddress">Street Address</span>
          </dt>
          <dd>{userAccountEntity.streetAddress}</dd>
          <dt>
            <span id="city">City</span>
          </dt>
          <dd>{userAccountEntity.city}</dd>
          <dt>
            <span id="state">State</span>
          </dt>
          <dd>{userAccountEntity.state}</dd>
          <dt>
            <span id="country">Country</span>
          </dt>
          <dd>{userAccountEntity.country}</dd>
          <dt>
            <span id="zipcode">Zipcode</span>
          </dt>
          <dd>{userAccountEntity.zipcode}</dd>
          <dt>
            <span id="phoneNumber">Phone Number</span>
          </dt>
          <dd>{userAccountEntity.phoneNumber}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{userAccountEntity.email}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{userAccountEntity.isActive}</dd>
        </dl>
        <Button tag={Link} to="/user-account" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-account/${userAccountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAccountDetail;
