import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-bmr.reducer';

export const UserBMR = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userBMRList = useAppSelector(state => state.userBMR.entities);
  const loading = useAppSelector(state => state.userBMR.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="user-bmr-heading" data-cy="UserBMRHeading">
        User BMRS
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/user-bmr/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User BMR
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userBMRList && userBMRList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('idVersion')}>
                  Id Version <FontAwesomeIcon icon={getSortIconByFieldName('idVersion')} />
                </th>
                <th className="hand" onClick={sort('bmr')}>
                  Bmr <FontAwesomeIcon icon={getSortIconByFieldName('bmr')} />
                </th>
                <th className="hand" onClick={sort('dtCreated')}>
                  Dt Created <FontAwesomeIcon icon={getSortIconByFieldName('dtCreated')} />
                </th>
                <th className="hand" onClick={sort('dtModified')}>
                  Dt Modified <FontAwesomeIcon icon={getSortIconByFieldName('dtModified')} />
                </th>
                <th>
                  User Profile <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userBMRList.map((userBMR, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-bmr/${userBMR.id}`} color="link" size="sm">
                      {userBMR.id}
                    </Button>
                  </td>
                  <td>{userBMR.idVersion}</td>
                  <td>{userBMR.bmr}</td>
                  <td>{userBMR.dtCreated ? <TextFormat type="date" value={userBMR.dtCreated} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{userBMR.dtModified ? <TextFormat type="date" value={userBMR.dtModified} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{userBMR.userProfile ? <Link to={`/user-profile/${userBMR.userProfile.id}`}>{userBMR.userProfile.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-bmr/${userBMR.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/user-bmr/${userBMR.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-bmr/${userBMR.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No User BMRS found</div>
        )}
      </div>
    </div>
  );
};

export default UserBMR;
