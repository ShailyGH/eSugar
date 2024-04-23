export interface IUserAccount {
  id?: number;
  loginName?: string | null;
  password?: string | null;
  streetAddress?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  zipcode?: string | null;
  phoneNumber?: number | null;
  email?: string | null;
  isActive?: string | null;
}

export const defaultValue: Readonly<IUserAccount> = {};
