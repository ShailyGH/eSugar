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
import { IUserBMR } from 'app/shared/model/user-bmr.model';
import { getEntity, updateEntity, createEntity, reset } from './user-bmr.reducer';

export const UserBMRUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const userBMREntity = useAppSelector(state => state.userBMR.entity);
  const loading = useAppSelector(state => state.userBMR.loading);
  const updating = useAppSelector(state => state.userBMR.updating);
  const updateSuccess = useAppSelector(state => state.userBMR.updateSuccess);

  const handleClose = () => {
    navigate('/user-bmr');
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
    if (values.idVersion !== undefined && typeof values.idVersion !== 'number') {
      values.idVersion = Number(values.idVersion);
    }
    if (values.bmr !== undefined && typeof values.bmr !== 'number') {
      values.bmr = Number(values.bmr);
    }
    values.dtCreated = convertDateTimeToServer(values.dtCreated);
    values.dtModified = convertDateTimeToServer(values.dtModified);

    const entity = {
      ...userBMREntity,
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
          dtCreated: displayDefaultDateTime(),
          dtModified: displayDefaultDateTime(),
        }
      : {
          ...userBMREntity,
          dtCreated: convertDateTimeFromServer(userBMREntity.dtCreated),
          dtModified: convertDateTimeFromServer(userBMREntity.dtModified),
          userProfile: userBMREntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.userBMR.home.createOrEditLabel" data-cy="UserBMRCreateUpdateHeading">
            Create or edit a User BMR
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="user-bmr-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Id Version" id="user-bmr-idVersion" name="idVersion" data-cy="idVersion" type="text" />
              <ValidatedField label="Bmr" id="user-bmr-bmr" name="bmr" data-cy="bmr" type="text" />
              <ValidatedField
                label="Dt Created"
                id="user-bmr-dtCreated"
                name="dtCreated"
                data-cy="dtCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Dt Modified"
                id="user-bmr-dtModified"
                name="dtModified"
                data-cy="dtModified"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="user-bmr-userProfile" name="userProfile" data-cy="userProfile" label="User Profile" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-bmr" replace color="info">
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

export default UserBMRUpdate;
