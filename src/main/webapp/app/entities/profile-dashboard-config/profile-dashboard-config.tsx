import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './profile-dashboard-config.reducer';

export const ProfileDashboardConfig = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const profileDashboardConfigList = useAppSelector(state => state.profileDashboardConfig.entities);
  const loading = useAppSelector(state => state.profileDashboardConfig.loading);

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
      <h2 id="profile-dashboard-config-heading" data-cy="ProfileDashboardConfigHeading">
        Profile Dashboard Configs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link
            to="/profile-dashboard-config/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Profile Dashboard Config
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {profileDashboardConfigList && profileDashboardConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('isBloodGlucoseShown')}>
                  Is Blood Glucose Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBloodGlucoseShown')} />
                </th>
                <th className="hand" onClick={sort('isBloodPressureShown')}>
                  Is Blood Pressure Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBloodPressureShown')} />
                </th>
                <th className="hand" onClick={sort('isBodyCompositionShown')}>
                  Is Body Composition Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBodyCompositionShown')} />
                </th>
                <th className="hand" onClick={sort('isBloodCholesterolShown')}>
                  Is Blood Cholesterol Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBloodCholesterolShown')} />
                </th>
                <th className="hand" onClick={sort('isBodyHeightShown')}>
                  Is Body Height Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBodyHeightShown')} />
                </th>
                <th className="hand" onClick={sort('isBodyWeightShown')}>
                  Is Body Weight Shown <FontAwesomeIcon icon={getSortIconByFieldName('isBodyWeightShown')} />
                </th>
                <th className="hand" onClick={sort('isCaloriesBurntShown')}>
                  Is Calories Burnt Shown <FontAwesomeIcon icon={getSortIconByFieldName('isCaloriesBurntShown')} />
                </th>
                <th>
                  User Profile <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {profileDashboardConfigList.map((profileDashboardConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/profile-dashboard-config/${profileDashboardConfig.id}`} color="link" size="sm">
                      {profileDashboardConfig.id}
                    </Button>
                  </td>
                  <td>{profileDashboardConfig.isBloodGlucoseShown}</td>
                  <td>{profileDashboardConfig.isBloodPressureShown}</td>
                  <td>{profileDashboardConfig.isBodyCompositionShown}</td>
                  <td>{profileDashboardConfig.isBloodCholesterolShown}</td>
                  <td>{profileDashboardConfig.isBodyHeightShown}</td>
                  <td>{profileDashboardConfig.isBodyWeightShown}</td>
                  <td>{profileDashboardConfig.isCaloriesBurntShown}</td>
                  <td>
                    {profileDashboardConfig.userProfile ? (
                      <Link to={`/user-profile/${profileDashboardConfig.userProfile.id}`}>{profileDashboardConfig.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/profile-dashboard-config/${profileDashboardConfig.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/profile-dashboard-config/${profileDashboardConfig.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/profile-dashboard-config/${profileDashboardConfig.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Profile Dashboard Configs found</div>
        )}
      </div>
    </div>
  );
};

export default ProfileDashboardConfig;
