import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserAccount } from 'app/shared/model/user-account.model';
import { getEntity, updateEntity, createEntity, reset } from './user-account.reducer';

export const UserAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userAccountEntity = useAppSelector(state => state.userAccount.entity);
  const loading = useAppSelector(state => state.userAccount.loading);
  const updating = useAppSelector(state => state.userAccount.updating);
  const updateSuccess = useAppSelector(state => state.userAccount.updateSuccess);

  const handleClose = () => {
    navigate('/user-account');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.phoneNumber !== undefined && typeof values.phoneNumber !== 'number') {
      values.phoneNumber = Number(values.phoneNumber);
    }

    const entity = {
      ...userAccountEntity,
      ...values,
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
          ...userAccountEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.userAccount.home.createOrEditLabel" data-cy="UserAccountCreateUpdateHeading">
            Create or edit a User Account
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="user-account-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField label="Login Name" id="user-account-loginName" name="loginName" data-cy="loginName" type="text" />
              <ValidatedField label="Password" id="user-account-password" name="password" data-cy="password" type="text" />
              <ValidatedField
                label="Street Address"
                id="user-account-streetAddress"
                name="streetAddress"
                data-cy="streetAddress"
                type="text"
              />
              <ValidatedField label="City" id="user-account-city" name="city" data-cy="city" type="text" />
              <ValidatedField label="State" id="user-account-state" name="state" data-cy="state" type="text" />
              <ValidatedField label="Country" id="user-account-country" name="country" data-cy="country" type="text" />
              <ValidatedField label="Zipcode" id="user-account-zipcode" name="zipcode" data-cy="zipcode" type="text" />
              <ValidatedField label="Phone Number" id="user-account-phoneNumber" name="phoneNumber" data-cy="phoneNumber" type="text" />
              <ValidatedField label="Email" id="user-account-email" name="email" data-cy="email" type="text" />
              <ValidatedField label="Is Active" id="user-account-isActive" name="isActive" data-cy="isActive" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-account" replace color="info">
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

export default UserAccountUpdate;
