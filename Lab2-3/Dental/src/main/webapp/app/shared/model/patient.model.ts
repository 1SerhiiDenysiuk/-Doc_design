import { ITreatment } from 'app/shared/model/treatment.model';
import { IDoctor } from 'app/shared/model/doctor.model';

export interface IPatient {
  id?: number;
  name?: string;
  tel?: number;
  address?: string;
  age?: number;
  sex?: string;
  doctorID?: number;
  treatments?: ITreatment[];
  doctors?: IDoctor;
}

export class Patient implements IPatient {
  constructor(
    public id?: number,
    public name?: string,
    public tel?: number,
    public address?: string,
    public age?: number,
    public sex?: string,
    public doctorID?: number,
    public treatments?: ITreatment[],
    public doctors?: IDoctor
  ) {}
}
