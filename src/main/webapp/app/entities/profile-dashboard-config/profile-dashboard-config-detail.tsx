import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './profile-dashboard-config.reducer';

export const ProfileDashboardConfigDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const profileDashboardConfigEntity = useAppSelector(state => state.profileDashboardConfig.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="profileDashboardConfigDetailsHeading">Profile Dashboard Config</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{profileDashboardConfigEntity.id}</dd>
          <dt>
            <span id="isBloodGlucoseShown">Is Blood Glucose Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBloodGlucoseShown}</dd>
          <dt>
            <span id="isBloodPressureShown">Is Blood Pressure Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBloodPressureShown}</dd>
          <dt>
            <span id="isBodyCompositionShown">Is Body Composition Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBodyCompositionShown}</dd>
          <dt>
            <span id="isBloodCholesterolShown">Is Blood Cholesterol Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBloodCholesterolShown}</dd>
          <dt>
            <span id="isBodyHeightShown">Is Body Height Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBodyHeightShown}</dd>
          <dt>
            <span id="isBodyWeightShown">Is Body Weight Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isBodyWeightShown}</dd>
          <dt>
            <span id="isCaloriesBurntShown">Is Calories Burnt Shown</span>
          </dt>
          <dd>{profileDashboardConfigEntity.isCaloriesBurntShown}</dd>
          <dt>User Profile</dt>
          <dd>{profileDashboardConfigEntity.userProfile ? profileDashboardConfigEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/profile-dashboard-config" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profile-dashboard-config/${profileDashboardConfigEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfileDashboardConfigDetail;
