import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './reservation.reducer';
import { getEntities as getInventories } from './../inventory/inventory.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IInventory } from 'app/shared/model/inventory.model';
import { IUser, userDisplayName } from 'app/shared/model/user.model';

export const Reservation = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const reservationList = useAppSelector(state => state.reservation.entities);
  const loading = useAppSelector(state => state.reservation.loading);

  const inventoryList = useAppSelector(state => state.inventory.entities);
  const users = useAppSelector(state => state.userManagement.users);

  const getAllEntities = () => {
    // load dependents
    dispatch(getInventories({}));
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

  function getInventory(reservation) {
    return inventoryList.find(it => it.id === reservation.inventory.id);
  }

  function getInventoryDisplayName(inventory: IInventory) {
    return inventory.name;
  }

  function getUser(reservation) {
    return users.find(it => reservation.user.id === it.id);
  }
  return (
    <div>
      <h2 id="reservation-heading" data-cy="ReservationHeading">
        Reservations
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/reservation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Reservation
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {reservationList && reservationList.length > 0 && inventoryList && inventoryList.length > 0 && users && users.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('reservedAt')}>
                  Reserved At <FontAwesomeIcon icon={getSortIconByFieldName('reservedAt')} />
                </th>
                <th>
                  Inventory <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  User <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reservationList.map((reservation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/reservation/${reservation.id}`} color="link" size="sm">
                      {reservation.id}
                    </Button>
                  </td>
                  <td>
                    {reservation.reservedAt ? <TextFormat type="date" value={reservation.reservedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {reservation.inventory ? (
                      <Link to={`/inventory/${reservation.inventory.id}`}>{getInventoryDisplayName(getInventory(reservation))}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{reservation.user ? userDisplayName(getUser(reservation)) : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/reservation/${reservation.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/reservation/${reservation.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/reservation/${reservation.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Return</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Reservations found</div>
        )}
      </div>
    </div>
  );
};

export default Reservation;
