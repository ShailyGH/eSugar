import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './blood-glucose.reducer';

export const BloodGlucoseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bloodGlucoseEntity = useAppSelector(state => state.bloodGlucose.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bloodGlucoseDetailsHeading">Blood Glucose</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bloodGlucoseEntity.id}</dd>
          <dt>
            <span id="measurement">Measurement</span>
          </dt>
          <dd>{bloodGlucoseEntity.measurement}</dd>
          <dt>
            <span id="measurementContent">Measurement Content</span>
          </dt>
          <dd>{bloodGlucoseEntity.measurementContent}</dd>
          <dt>
            <span id="measurementType">Measurement Type</span>
          </dt>
          <dd>{bloodGlucoseEntity.measurementType}</dd>
          <dt>User Profile</dt>
          <dd>{bloodGlucoseEntity.userProfile ? bloodGlucoseEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/blood-glucose" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/blood-glucose/${bloodGlucoseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BloodGlucoseDetail;
