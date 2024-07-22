export interface Command {
  id: number;
  command: string;
  description: string;
}

export interface Device {
  id: number;
  status: boolean;
  identifier: string;
  manufacturer: string;
  url: string;
  user: User;
  commands: Command[];
}

export interface User {
  id: number;
  username: string;
  password: string;
  role: string;
}

export interface Measurement {
  id: number;
  createdAt: string;
  result: string;
  device: Device;
  command: Command;
}