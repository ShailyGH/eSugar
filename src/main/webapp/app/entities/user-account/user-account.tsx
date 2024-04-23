import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-account.reducer';

export const UserAccount = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userAccountList = useAppSelector(state => state.userAccount.entities);
  const loading = useAppSelector(state => state.userAccount.loading);

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
      <h2 id="user-account-heading" data-cy="UserAccountHeading">
        User Accounts
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/user-account/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User Account
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAccountList && userAccountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  Id <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('loginName')}>
                  Login Name <FontAwesomeIcon icon={getSortIconByFieldName('loginName')} />
                </th>
                <th className="hand" onClick={sort('password')}>
                  Password <FontAwesomeIcon icon={getSortIconByFieldName('password')} />
                </th>
                <th className="hand" onClick={sort('streetAddress')}>
                  Street Address <FontAwesomeIcon icon={getSortIconByFieldName('streetAddress')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  City <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  State <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  Country <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('zipcode')}>
                  Zipcode <FontAwesomeIcon icon={getSortIconByFieldName('zipcode')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  Phone Number <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  Email <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  Is Active <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAccountList.map((userAccount, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-account/${userAccount.id}`} color="link" size="sm">
                      {userAccount.id}
                    </Button>
                  </td>
                  <td>{userAccount.loginName}</td>
                  <td>{userAccount.password}</td>
                  <td>{userAccount.streetAddress}</td>
                  <td>{userAccount.city}</td>
                  <td>{userAccount.state}</td>
                  <td>{userAccount.country}</td>
                  <td>{userAccount.zipcode}</td>
                  <td>{userAccount.phoneNumber}</td>
                  <td>{userAccount.email}</td>
                  <td>{userAccount.isActive}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-account/${userAccount.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/user-account/${userAccount.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-account/${userAccount.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No User Accounts found</div>
        )}
      </div>
    </div>
  );
};

export default UserAccount;
