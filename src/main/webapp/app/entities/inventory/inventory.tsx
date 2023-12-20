import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './inventory.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUser, userDisplayName } from 'app/shared/model/user.model';

export const Inventory = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const inventoryList = useAppSelector(state => state.inventory.entities);
  const loading = useAppSelector(state => state.inventory.loading);

  const users = useAppSelector(state => state.userManagement.users);

  const getAllEntities = () => {
    dispatch(getUsers({}));

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

  function getUser(reservation): IUser {
    return users.find(it => reservation.user.id === it.id) as IUser;
  }

  return (
    <div>
      <h2 id="inventory-heading" data-cy="InventoryHeading">
        Inventories
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/inventory/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Inventory
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {inventoryList && inventoryList.length > 0 && users && users.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Name <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  Quantity <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Available <FontAwesomeIcon icon={getSortIconByFieldName('available')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Reserved By <FontAwesomeIcon icon={getSortIconByFieldName('reservedBy')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  Reserved At <FontAwesomeIcon icon={getSortIconByFieldName('reservedAt')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {inventoryList.map((inventory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/inventory/${inventory.id}`} color="link" size="sm">
                      {inventory.id}
                    </Button>
                  </td>
                  <td>{inventory.name}</td>
                  <td>{inventory.quantity}</td>
                  <td>{inventory.available ? 'Yes' : 'No'}</td>

                  <td>
                    {inventory.reservations
                      ? inventory.reservations.map((reservation, ri) => (
                          <div key={`reservation-${ri}-user`}>{userDisplayName(getUser(reservation))}</div>
                        ))
                      : null}
                  </td>

                  <td>
                    {inventory.reservations
                      ? inventory.reservations.map((reservation, ri) => (
                          <div key={`reservation-${ri}-reservedat`}>
                            <TextFormat type="date" value={reservation.reservedAt} format={APP_DATE_FORMAT} />
                          </div>
                        ))
                      : null}
                  </td>

                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/inventory/${inventory.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/inventory/${inventory.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/inventory/${inventory.id}/delete`)}
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
          !loading && <div className="alert alert-warning">No Inventories found</div>
        )}
      </div>
    </div>
  );
};

export default Inventory;
