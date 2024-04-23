import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-bmr.reducer';

export const UserBMRDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userBMREntity = useAppSelector(state => state.userBMR.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userBMRDetailsHeading">User BMR</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{userBMREntity.id}</dd>
          <dt>
            <span id="idVersion">Id Version</span>
          </dt>
          <dd>{userBMREntity.idVersion}</dd>
          <dt>
            <span id="bmr">Bmr</span>
          </dt>
          <dd>{userBMREntity.bmr}</dd>
          <dt>
            <span id="dtCreated">Dt Created</span>
          </dt>
          <dd>{userBMREntity.dtCreated ? <TextFormat value={userBMREntity.dtCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dtModified">Dt Modified</span>
          </dt>
          <dd>{userBMREntity.dtModified ? <TextFormat value={userBMREntity.dtModified} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>User Profile</dt>
          <dd>{userBMREntity.userProfile ? userBMREntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-bmr" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-bmr/${userBMREntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserBMRDetail;
