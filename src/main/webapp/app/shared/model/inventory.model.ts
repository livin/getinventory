import { IReservation } from 'app/shared/model/reservation.model';

export interface IInventory {
  id?: number;
  name?: string;
  quantity?: number;
  reservations?: IReservation[] | null;
}

export const defaultValue: Readonly<IInventory> = {};
