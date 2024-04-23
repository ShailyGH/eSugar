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

import { getEntities } from './activity-log.reducer';

export const ActivityLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const activityLogList = useAppSelector(state => state.activityLog.entities);
  const loading = useAppSelector(state => state.activityLog.loading);

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
      <h2 id="activity-log-heading" data-cy="ActivityLogHeading">
        Activity Logs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/activity-log/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Activity Log
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {activityLogList && activityLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startDateTime')}>
                  Start Date Time <FontAwesomeIcon icon={getSortIconByFieldName('startDateTime')} />
                </th>
                <th className="hand" onClick={sort('endDateTime')}>
                  End Date Time <FontAwesomeIcon icon={getSortIconByFieldName('endDateTime')} />
                </th>
                <th className="hand" onClick={sort('distanceCovered')}>
                  Distance Covered <FontAwesomeIcon icon={getSortIconByFieldName('distanceCovered')} />
                </th>
                <th className="hand" onClick={sort('stepsCount')}>
                  Steps Count <FontAwesomeIcon icon={getSortIconByFieldName('stepsCount')} />
                </th>
                <th className="hand" onClick={sort('caloriesBurnt')}>
                  Calories Burnt <FontAwesomeIcon icon={getSortIconByFieldName('caloriesBurnt')} />
                </th>
                <th>
                  User Profile <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activityLogList.map((activityLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/activity-log/${activityLog.id}`} color="link" size="sm">
                      {activityLog.id}
                    </Button>
                  </td>
                  <td>
                    {activityLog.startDateTime ? (
                      <TextFormat type="date" value={activityLog.startDateTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {activityLog.endDateTime ? <TextFormat type="date" value={activityLog.endDateTime} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{activityLog.distanceCovered}</td>
                  <td>{activityLog.stepsCount}</td>
                  <td>{activityLog.caloriesBurnt}</td>
                  <td>
                    {activityLog.userProfile ? (
                      <Link to={`/user-profile/${activityLog.userProfile.id}`}>{activityLog.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/activity-log/${activityLog.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/activity-log/${activityLog.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/activity-log/${activityLog.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Activity Logs found</div>
        )}
      </div>
    </div>
  );
};

export default ActivityLog;
