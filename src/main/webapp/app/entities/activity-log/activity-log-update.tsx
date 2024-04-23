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
import { IActivityLog } from 'app/shared/model/activity-log.model';
import { getEntity, updateEntity, createEntity, reset } from './activity-log.reducer';

export const ActivityLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const activityLogEntity = useAppSelector(state => state.activityLog.entity);
  const loading = useAppSelector(state => state.activityLog.loading);
  const updating = useAppSelector(state => state.activityLog.updating);
  const updateSuccess = useAppSelector(state => state.activityLog.updateSuccess);

  const handleClose = () => {
    navigate('/activity-log');
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
    values.startDateTime = convertDateTimeToServer(values.startDateTime);
    values.endDateTime = convertDateTimeToServer(values.endDateTime);
    if (values.distanceCovered !== undefined && typeof values.distanceCovered !== 'number') {
      values.distanceCovered = Number(values.distanceCovered);
    }
    if (values.stepsCount !== undefined && typeof values.stepsCount !== 'number') {
      values.stepsCount = Number(values.stepsCount);
    }
    if (values.caloriesBurnt !== undefined && typeof values.caloriesBurnt !== 'number') {
      values.caloriesBurnt = Number(values.caloriesBurnt);
    }

    const entity = {
      ...activityLogEntity,
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
      ? {
          startDateTime: displayDefaultDateTime(),
          endDateTime: displayDefaultDateTime(),
        }
      : {
          ...activityLogEntity,
          startDateTime: convertDateTimeFromServer(activityLogEntity.startDateTime),
          endDateTime: convertDateTimeFromServer(activityLogEntity.endDateTime),
          userProfile: activityLogEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.activityLog.home.createOrEditLabel" data-cy="ActivityLogCreateUpdateHeading">
            Create or edit a Activity Log
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="activity-log-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Start Date Time"
                id="activity-log-startDateTime"
                name="startDateTime"
                data-cy="startDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="End Date Time"
                id="activity-log-endDateTime"
                name="endDateTime"
                data-cy="endDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Distance Covered"
                id="activity-log-distanceCovered"
                name="distanceCovered"
                data-cy="distanceCovered"
                type="text"
              />
              <ValidatedField label="Steps Count" id="activity-log-stepsCount" name="stepsCount" data-cy="stepsCount" type="text" />
              <ValidatedField
                label="Calories Burnt"
                id="activity-log-caloriesBurnt"
                name="caloriesBurnt"
                data-cy="caloriesBurnt"
                type="text"
              />
              <ValidatedField id="activity-log-userProfile" name="userProfile" data-cy="userProfile" label="User Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity-log" replace color="info">
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

export default ActivityLogUpdate;
