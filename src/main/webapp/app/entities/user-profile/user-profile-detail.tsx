import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">User Profile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="userProfileName">User Profile Name</span>
          </dt>
          <dd>{userProfileEntity.userProfileName}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{userProfileEntity.email}</dd>
          <dt>
            <span id="isReportSharingEnabled">Is Report Sharing Enabled</span>
          </dt>
          <dd>{userProfileEntity.isReportSharingEnabled}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{userProfileEntity.isActive}</dd>
          <dt>User Account</dt>
          <dd>{userProfileEntity.userAccount ? userProfileEntity.userAccount.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
