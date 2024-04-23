import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './characteristic-data.reducer';

export const CharacteristicDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const characteristicDataEntity = useAppSelector(state => state.characteristicData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="characteristicDataDetailsHeading">Characteristic Data</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{characteristicDataEntity.id}</dd>
          <dt>
            <span id="dateOfBirth">Date Of Birth</span>
          </dt>
          <dd>
            {characteristicDataEntity.dateOfBirth ? (
              <TextFormat value={characteristicDataEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{characteristicDataEntity.gender}</dd>
          <dt>
            <span id="bloodType">Blood Type</span>
          </dt>
          <dd>{characteristicDataEntity.bloodType}</dd>
          <dt>User Profile</dt>
          <dd>{characteristicDataEntity.userProfile ? characteristicDataEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/characteristic-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/characteristic-data/${characteristicDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CharacteristicDataDetail;
