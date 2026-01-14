import { Component, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserApiService, UserDto } from './user-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  logoText = 'YouTube Demo';

  // Form
  username = signal('');
  email = signal('');

  // UI state
  loading = signal(false);
  error = signal<string | null>(null);

  // data
  users = signal<UserDto[]>([]);

  // inline edit state (only one row at a time)
  editingId = signal<number | null>(null);
  editUsername = signal('');
  private originalUsername = ''; // for cancel

  // validation (create)
  usernameError = computed(() => {
    const v = this.username().trim();
    if (!v) return 'Username is required';
    if (v.length < 2) return 'Username must be at least 2 characters';
    return null;
  });

  emailError = computed(() => {
    const v = this.email().trim();
    if (!v) return 'Email is required';
    const ok = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v);
    return ok ? null : 'Email is not valid';
  });

  canSave = computed(() => !this.loading() && !this.usernameError() && !this.emailError());

  // validation (edit)
  canConfirmEdit = computed(() => {
    const id = this.editingId();
    if (id == null) return false;
    const v = this.editUsername().trim();
    if (!v) return false;
    if (v.length < 2) return false;
    return !this.loading();
  });

  constructor(private api: UserApiService) {
    this.refresh();
  }

  refresh() {
    this.loading.set(true);
    this.error.set(null);

    this.api.getAll().subscribe({
      next: (items) => {
        this.users.set(items);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? err?.message ?? 'Failed to load users');
        this.loading.set(false);
      },
    });
  }

  save() {
    if (!this.canSave()) return;

    this.loading.set(true);
    this.error.set(null);

    const username = this.username().trim();
    const email = this.email().trim();

    this.api.create({ username, email }).subscribe({
      next: (created) => {
        this.users.set([created, ...this.users()]);
        this.username.set('');
        this.email.set('');
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? err?.message ?? 'Create failed');
        this.loading.set(false);
      },
    });
  }

  remove(userId: number) {
    // if deleting the row being edited, cancel edit first
    if (this.editingId() === userId) this.cancelEdit();

    this.loading.set(true);
    this.error.set(null);

    this.api.delete(userId).subscribe({
      next: () => {
        this.users.set(this.users().filter((u) => u.id !== userId));
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? err?.message ?? 'Delete failed');
        this.loading.set(false);
      },
    });
  }

  startEdit(u: UserDto) {
    // auto-cancel any previous edit
    if (this.editingId() != null && this.editingId() !== u.id) {
      this.cancelEdit();
    }

    this.editingId.set(u.id);
    this.originalUsername = u.username;
    this.editUsername.set(u.username);
    this.error.set(null);
  }

  cancelEdit() {
    this.editingId.set(null);
    this.editUsername.set('');
    this.originalUsername = '';
  }

  confirmEdit() {
    if (!this.canConfirmEdit()) return;

    const id = this.editingId();
    if (id == null) return;

    const newName = this.editUsername().trim();
    if (newName === this.originalUsername) {
      // no changes -> just close edit mode
      this.cancelEdit();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.api.updateUsername(id, newName).subscribe({
      next: (updated) => {
        this.users.set(
          this.users().map((u) => (u.id === updated.id ? updated : u)),
        );
        this.loading.set(false);
        this.cancelEdit();
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? err?.message ?? 'Update failed');
        this.loading.set(false);
      },
    });
  }

  trackById = (_: number, u: UserDto) => u.id;
}
