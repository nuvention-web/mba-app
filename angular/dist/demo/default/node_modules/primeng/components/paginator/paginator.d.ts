import { EventEmitter } from '@angular/core';
import { SelectItem } from '../common/selectitem';
export declare class Paginator {
    pageLinkSize: number;
    onPageChange: EventEmitter<any>;
    style: any;
    styleClass: string;
    alwaysShow: boolean;
    pageLinks: number[];
    _totalRecords: number;
    _first: number;
    _rows: number;
    _rowsPerPageOptions: number[];
    rowsPerPageItems: SelectItem[];
    totalRecords: number;
    first: number;
    rows: number;
    rowsPerPageOptions: number[];
    isFirstPage(): boolean;
    isLastPage(): boolean;
    getPageCount(): number;
    calculatePageLinkBoundaries(): number[];
    updatePageLinks(): void;
    changePage(p: number): void;
    getPage(): number;
    changePageToFirst(event: any): void;
    changePageToPrev(event: any): void;
    changePageToNext(event: any): void;
    changePageToLast(event: any): void;
    onPageLinkClick(event: any, page: any): void;
    onRppChange(event: any): void;
}
export declare class PaginatorModule {
}
