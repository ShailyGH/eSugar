import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivityLog } from 'app/shared/model/activity-log.model';
import { getEntities as getActivityLogs } from 'app/entities/activity-log/activity-log.reducer';
import { IActivity } from 'app/shared/model/activity.model';
import { getEntity, updateEntity, createEntity, reset } from './activity.reducer';

export const ActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activityLogs = useAppSelector(state => state.activityLog.entities);
  const activityEntity = useAppSelector(state => state.activity.entity);
  const loading = useAppSelector(state => state.activity.loading);
  const updating = useAppSelector(state => state.activity.updating);
  const updateSuccess = useAppSelector(state => state.activity.updateSuccess);

  const handleClose = () => {
    navigate('/activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActivityLogs({}));
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
    if (values.activityMultiplier !== undefined && typeof values.activityMultiplier !== 'number') {
      values.activityMultiplier = Number(values.activityMultiplier);
    }

    const entity = {
      ...activityEntity,
      ...values,
      activityLog: activityLogs.find(it => it.id.toString() === values.activityLog?.toString()),
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
          ...activityEntity,
          activityLog: activityEntity?.activityLog?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.activity.home.createOrEditLabel" data-cy="ActivityCreateUpdateHeading">
            Create or edit a Activity
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="activity-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Activity Name" id="activity-activityName" name="activityName" data-cy="activityName" type="text" />
              <ValidatedField
                label="Activity Multiplier"
                id="activity-activityMultiplier"
                name="activityMultiplier"
                data-cy="activityMultiplier"
                type="text"
              />
              <ValidatedField id="activity-activityLog" name="activityLog" data-cy="activityLog" label="Activity Log" type="select">
                <option value="" key="0" />
                {activityLogs
                  ? activityLogs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activity" replace color="info">
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

export default ActivityUpdate;
