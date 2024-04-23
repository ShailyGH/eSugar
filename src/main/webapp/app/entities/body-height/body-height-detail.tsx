import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './body-height.reducer';

export const BodyHeightDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bodyHeightEntity = useAppSelector(state => state.bodyHeight.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bodyHeightDetailsHeading">Body Height</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bodyHeightEntity.id}</dd>
          <dt>
            <span id="height">Height</span>
          </dt>
          <dd>{bodyHeightEntity.height}</dd>
          <dt>User Profile</dt>
          <dd>{bodyHeightEntity.userProfile ? bodyHeightEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/body-height" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/body-height/${bodyHeightEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BodyHeightDetail;
