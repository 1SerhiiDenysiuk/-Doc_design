import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProcedure } from 'app/shared/model/procedure.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProcedureService } from './procedure.service';
import { ProcedureDeleteDialogComponent } from './procedure-delete-dialog.component';

@Component({
  selector: 'jhi-procedure',
  templateUrl: './procedure.component.html',
})
export class ProcedureComponent implements OnInit, OnDestroy {
  procedures: IProcedure[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected procedureService: ProcedureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.procedures = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.procedureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IProcedure[]>) => this.paginateProcedures(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.procedures = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInProcedures();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IProcedure): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProcedures(): void {
    this.eventSubscriber = this.eventManager.subscribe('procedureListModification', () => this.reset());
  }

  delete(procedure: IProcedure): void {
    const modalRef = this.modalService.open(ProcedureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.procedure = procedure;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProcedures(data: IProcedure[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.procedures.push(data[i]);
      }
    }
  }
}
