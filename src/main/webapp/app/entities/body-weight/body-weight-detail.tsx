import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './body-weight.reducer';

export const BodyWeightDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bodyWeightEntity = useAppSelector(state => state.bodyWeight.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bodyWeightDetailsHeading">Body Weight</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{bodyWeightEntity.id}</dd>
          <dt>
            <span id="weight">Weight</span>
          </dt>
          <dd>{bodyWeightEntity.weight}</dd>
          <dt>User Profile</dt>
          <dd>{bodyWeightEntity.userProfile ? bodyWeightEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/body-weight" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/body-weight/${bodyWeightEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BodyWeightDetail;
