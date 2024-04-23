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
import { IBloodGlucose } from 'app/shared/model/blood-glucose.model';
import { getEntity, updateEntity, createEntity, reset } from './blood-glucose.reducer';

export const BloodGlucoseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const bloodGlucoseEntity = useAppSelector(state => state.bloodGlucose.entity);
  const loading = useAppSelector(state => state.bloodGlucose.loading);
  const updating = useAppSelector(state => state.bloodGlucose.updating);
  const updateSuccess = useAppSelector(state => state.bloodGlucose.updateSuccess);

  const handleClose = () => {
    navigate('/blood-glucose');
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
    if (values.measurement !== undefined && typeof values.measurement !== 'number') {
      values.measurement = Number(values.measurement);
    }

    const entity = {
      ...bloodGlucoseEntity,
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
          ...bloodGlucoseEntity,
          userProfile: bloodGlucoseEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.bloodGlucose.home.createOrEditLabel" data-cy="BloodGlucoseCreateUpdateHeading">
            Create or edit a Blood Glucose
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
                <ValidatedField name="id" required readOnly id="blood-glucose-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Measurement" id="blood-glucose-measurement" name="measurement" data-cy="measurement" type="text" />
              <ValidatedField
                label="Measurement Content"
                id="blood-glucose-measurementContent"
                name="measurementContent"
                data-cy="measurementContent"
                type="text"
              />
              <ValidatedField
                label="Measurement Type"
                id="blood-glucose-measurementType"
                name="measurementType"
                data-cy="measurementType"
                type="text"
              />
              <ValidatedField id="blood-glucose-userProfile" name="userProfile" data-cy="userProfile" label="User Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/blood-glucose" replace color="info">
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

export default BloodGlucoseUpdate;
