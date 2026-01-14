import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type UserDto = {
  id: number;
  username: string;
  email: string;
};

export type CreateUserDto = {
  username: string;
  email: string;
};

@Injectable({ providedIn: 'root' })
export class UserApiService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>('/api/users');
  }

  create(req: CreateUserDto): Observable<UserDto> {
    return this.http.post<UserDto>('/api/users', req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/users/${id}`);
  }

  updateUsername(id: number, username: string): Observable<UserDto> {
    return this.http.patch<UserDto>(`/api/users/${id}/username`, { username });
  }
}
