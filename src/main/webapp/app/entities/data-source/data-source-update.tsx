import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBodyVitalsLog } from 'app/shared/model/body-vitals-log.model';
import { getEntities as getBodyVitalsLogs } from 'app/entities/body-vitals-log/body-vitals-log.reducer';
import { IDataSource } from 'app/shared/model/data-source.model';
import { getEntity, updateEntity, createEntity, reset } from './data-source.reducer';

export const DataSourceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bodyVitalsLogs = useAppSelector(state => state.bodyVitalsLog.entities);
  const dataSourceEntity = useAppSelector(state => state.dataSource.entity);
  const loading = useAppSelector(state => state.dataSource.loading);
  const updating = useAppSelector(state => state.dataSource.updating);
  const updateSuccess = useAppSelector(state => state.dataSource.updateSuccess);

  const handleClose = () => {
    navigate('/data-source');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBodyVitalsLogs({}));
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
      ...dataSourceEntity,
      ...values,
      bodyVitalsLog: bodyVitalsLogs.find(it => it.id.toString() === values.bodyVitalsLog?.toString()),
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
          ...dataSourceEntity,
          bodyVitalsLog: dataSourceEntity?.bodyVitalsLog?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eSugarApp.dataSource.home.createOrEditLabel" data-cy="DataSourceCreateUpdateHeading">
            Create or edit a Data Source
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="data-source-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Source Name" id="data-source-sourceName" name="sourceName" data-cy="sourceName" type="text" />
              <ValidatedField
                id="data-source-bodyVitalsLog"
                name="bodyVitalsLog"
                data-cy="bodyVitalsLog"
                label="Body Vitals Log"
                type="select"
              >
                <option value="" key="0" />
                {bodyVitalsLogs
                  ? bodyVitalsLogs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/data-source" replace color="info">
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

export default DataSourceUpdate;
