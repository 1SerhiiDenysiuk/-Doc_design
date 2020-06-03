import { ITreatment } from 'app/shared/model/treatment.model';

export interface IProcedure {
  id?: number;
  name?: string;
  price?: number;
  treatments?: ITreatment;
}

export class Procedure implements IProcedure {
  constructor(public id?: number, public name?: string, public price?: number, public treatments?: ITreatment) {}
}
