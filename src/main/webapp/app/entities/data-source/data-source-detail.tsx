import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './data-source.reducer';

export const DataSourceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dataSourceEntity = useAppSelector(state => state.dataSource.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dataSourceDetailsHeading">Data Source</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{dataSourceEntity.id}</dd>
          <dt>
            <span id="sourceName">Source Name</span>
          </dt>
          <dd>{dataSourceEntity.sourceName}</dd>
          <dt>Body Vitals Log</dt>
          <dd>{dataSourceEntity.bodyVitalsLog ? dataSourceEntity.bodyVitalsLog.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/data-source" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/data-source/${dataSourceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DataSourceDetail;
