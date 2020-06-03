import { Moment } from 'moment';
import { IProcedure } from 'app/shared/model/procedure.model';
import { IPatient } from 'app/shared/model/patient.model';

export interface ITreatment {
  id?: number;
  patientID?: number;
  date?: Moment;
  procedures?: IProcedure[];
  patients?: IPatient;
}

export class Treatment implements ITreatment {
  constructor(
    public id?: number,
    public patientID?: number,
    public date?: Moment,
    public procedures?: IProcedure[],
    public patients?: IPatient
  ) {}
}
