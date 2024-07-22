import { HttpClient } from '@angular/common/http';
import { firstValueFrom, map, Observable } from 'rxjs';

export class BaseAbstractService<T> {

    constructor(public http: HttpClient, public url: string) {
    }

    async get(id: number) {        
        if (id === 0) {
            return Promise.resolve(null);
        }

        return firstValueFrom(this.http.get<any>(`${this.url}/${id}`));
    }

    async save(bean: T | any) {
        console.log('url', this.url);
        
        return firstValueFrom(this.http.post<any>(this.url, bean));
    }

    async put(bean: T | any, id: number) {
        return firstValueFrom(this.http.put<any>(`${this.url}/${id}`, bean));
    }

    async delete(id: number | null) {
        if (!id) return Promise.resolve(null);

        return firstValueFrom(this.http.delete(`${this.url}/${id}`));
    }

    async listAutoComplete(): Promise<any> {
        return this.list(0, 10_000);
    }

    async list(page: number = 0, rpp: number = 10, filter?: string): Promise<any> {      
        const obj: any = {page, rpp};

        if (filter) {
            obj.filter = filter;
        }

        return firstValueFrom(this.http.get<any>(this.url, {params: obj}));
    }


}
