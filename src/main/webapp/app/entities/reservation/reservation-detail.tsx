import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">Reservation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="reservedAt">Reserved At</span>
          </dt>
          <dd>
            {reservationEntity.reservedAt ? <TextFormat value={reservationEntity.reservedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Inventory</dt>
          <dd>{reservationEntity.inventory ? reservationEntity.inventory.id : ''}</dd>
          <dt>User</dt>
          <dd>{reservationEntity.user ? reservationEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
