import { IReservation } from 'app/shared/model/reservation.model';

export interface IInventory {
  id?: number;
  name?: string;
  available?: boolean;
  reservation?: IReservation | null;
}

export const defaultValue: Readonly<IInventory> = {};
