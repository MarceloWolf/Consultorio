import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private currentThemeSubject = new BehaviorSubject<string>('dark');
  public currentTheme$ = this.currentThemeSubject.asObservable();

  constructor() {
    const savedTheme = localStorage.getItem('theme') || 'dark';
    this.setTheme(savedTheme);
  }

  public toggleTheme(): void {
    const nextTheme = this.currentThemeSubject.value === 'dark' ? 'light' : 'dark';
    this.setTheme(nextTheme);
  }

  public setTheme(theme: string): void {
    localStorage.setItem('theme', theme);
    this.currentThemeSubject.next(theme);
    
    const root = document.documentElement;
    if (theme === 'light') {
      root.classList.add('light-theme');
      root.classList.remove('dark-theme');
    } else {
      root.classList.add('dark-theme');
      root.classList.remove('light-theme');
    }
  }

  public isDark(): boolean {
    return this.currentThemeSubject.value === 'dark';
  }
}
