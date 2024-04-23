import { IBodyVitalsLog } from 'app/shared/model/body-vitals-log.model';

export interface IDataSource {
  id?: number;
  sourceName?: string | null;
  bodyVitalsLog?: IBodyVitalsLog | null;
}

export const defaultValue: Readonly<IDataSource> = {};
