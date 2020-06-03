import { IPatient } from 'app/shared/model/patient.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IDoctor {
  id?: number;
  name?: string;
  tel?: number;
  address?: string;
  specialization?: string;
  departmentID?: number;
  pacients?: IPatient[];
  departments?: IDepartment[];
}

export class Doctor implements IDoctor {
  constructor(
    public id?: number,
    public name?: string,
    public tel?: number,
    public address?: string,
    public specialization?: string,
    public departmentID?: number,
    public pacients?: IPatient[],
    public departments?: IDepartment[]
  ) {}
}
