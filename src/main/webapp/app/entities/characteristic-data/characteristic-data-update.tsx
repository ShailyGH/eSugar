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
import { ICharacteristicData } from 'app/shared/model/characteristic-data.model';
import { getEntity, updateEntity, createEntity, reset } from './characteristic-data.reducer';

export const CharacteristicDataUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const characteristicDataEntity = useAppSelector(state => state.characteristicData.entity);
  const loading = useAppSelector(state => state.characteristicData.loading);
  const updating = useAppSelector(state => state.characteristicData.updating);
  const updateSuccess = useAppSelector(state => state.characteristicData.updateSuccess);

  const handleClose = () => {
    navigate('/characteristic-data');
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
      ...characteristicDataEntity,
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
          ...characteristicDataEntity,
          userProfile: characteristicDataEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.characteristicData.home.createOrEditLabel" data-cy="CharacteristicDataCreateUpdateHeading">
            Create or edit a Characteristic Data
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
                <ValidatedField name="id" required readOnly id="characteristic-data-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Date Of Birth"
                id="characteristic-data-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="date"
              />
              <ValidatedField label="Gender" id="characteristic-data-gender" name="gender" data-cy="gender" type="text" />
              <ValidatedField label="Blood Type" id="characteristic-data-bloodType" name="bloodType" data-cy="bloodType" type="text" />
              <ValidatedField
                id="characteristic-data-userProfile"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/characteristic-data" replace color="info">
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

export default CharacteristicDataUpdate;
