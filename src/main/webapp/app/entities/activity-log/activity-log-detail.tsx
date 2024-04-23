import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activity-log.reducer';

export const ActivityLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activityLogEntity = useAppSelector(state => state.activityLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activityLogDetailsHeading">Activity Log</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activityLogEntity.id}</dd>
          <dt>
            <span id="startDateTime">Start Date Time</span>
          </dt>
          <dd>
            {activityLogEntity.startDateTime ? (
              <TextFormat value={activityLogEntity.startDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDateTime">End Date Time</span>
          </dt>
          <dd>
            {activityLogEntity.endDateTime ? (
              <TextFormat value={activityLogEntity.endDateTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="distanceCovered">Distance Covered</span>
          </dt>
          <dd>{activityLogEntity.distanceCovered}</dd>
          <dt>
            <span id="stepsCount">Steps Count</span>
          </dt>
          <dd>{activityLogEntity.stepsCount}</dd>
          <dt>
            <span id="caloriesBurnt">Calories Burnt</span>
          </dt>
          <dd>{activityLogEntity.caloriesBurnt}</dd>
          <dt>User Profile</dt>
          <dd>{activityLogEntity.userProfile ? activityLogEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/activity-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activity-log/${activityLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActivityLogDetail;
