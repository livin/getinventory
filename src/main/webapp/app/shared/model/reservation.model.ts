import dayjs from 'dayjs';
import { IInventory } from 'app/shared/model/inventory.model';

export interface IReservation {
  id?: number;
  reservedBy?: string;
  reservedAt?: dayjs.Dayjs | null;
  inventory?: IInventory | null;
}

export const defaultValue: Readonly<IReservation> = {};
