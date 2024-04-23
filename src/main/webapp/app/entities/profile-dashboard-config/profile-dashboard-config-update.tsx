import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IProfileDashboardConfig } from 'app/shared/model/profile-dashboard-config.model';
import { getEntity, updateEntity, createEntity, reset } from './profile-dashboard-config.reducer';

export const ProfileDashboardConfigUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const profileDashboardConfigEntity = useAppSelector(state => state.profileDashboardConfig.entity);
  const loading = useAppSelector(state => state.profileDashboardConfig.loading);
  const updating = useAppSelector(state => state.profileDashboardConfig.updating);
  const updateSuccess = useAppSelector(state => state.profileDashboardConfig.updateSuccess);

  const handleClose = () => {
    navigate('/profile-dashboard-config');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...profileDashboardConfigEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...profileDashboardConfigEntity,
          userProfile: profileDashboardConfigEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.profileDashboardConfig.home.createOrEditLabel" data-cy="ProfileDashboardConfigCreateUpdateHeading">
            Create or edit a Profile Dashboard Config
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="profile-dashboard-config-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Is Blood Glucose Shown"
                id="profile-dashboard-config-isBloodGlucoseShown"
                name="isBloodGlucoseShown"
                data-cy="isBloodGlucoseShown"
                type="text"
              />
              <ValidatedField
                label="Is Blood Pressure Shown"
                id="profile-dashboard-config-isBloodPressureShown"
                name="isBloodPressureShown"
                data-cy="isBloodPressureShown"
                type="text"
              />
              <ValidatedField
                label="Is Body Composition Shown"
                id="profile-dashboard-config-isBodyCompositionShown"
                name="isBodyCompositionShown"
                data-cy="isBodyCompositionShown"
                type="text"
              />
              <ValidatedField
                label="Is Blood Cholesterol Shown"
                id="profile-dashboard-config-isBloodCholesterolShown"
                name="isBloodCholesterolShown"
                data-cy="isBloodCholesterolShown"
                type="text"
              />
              <ValidatedField
                label="Is Body Height Shown"
                id="profile-dashboard-config-isBodyHeightShown"
                name="isBodyHeightShown"
                data-cy="isBodyHeightShown"
                type="text"
              />
              <ValidatedField
                label="Is Body Weight Shown"
                id="profile-dashboard-config-isBodyWeightShown"
                name="isBodyWeightShown"
                data-cy="isBodyWeightShown"
                type="text"
              />
              <ValidatedField
                label="Is Calories Burnt Shown"
                id="profile-dashboard-config-isCaloriesBurntShown"
                name="isCaloriesBurntShown"
                data-cy="isCaloriesBurntShown"
                type="text"
              />
              <ValidatedField
                id="profile-dashboard-config-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label="User Profile"
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/profile-dashboard-config" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProfileDashboardConfigUpdate;
