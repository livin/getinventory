import dayjs from 'dayjs';
import { IInventory } from 'app/shared/model/inventory.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReservation {
  id?: number;
  reservedAt?: dayjs.Dayjs | null;
  inventory?: IInventory | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IReservation> = {};
