export interface UserInfo {
  id: number
  username: string
  nickname: string | null
  avatar: string | null
  status: number
}

export interface LoginData {
  accessToken: string | null
  refreshToken: string | null
  tokenType: string | null
  expiresIn: number | null
  user: UserInfo
}

const AUTH_STORAGE_KEY = 'guanjian-journal-auth'

export function saveAuth(data: LoginData, remember: boolean) {
  const targetStorage = remember ? localStorage : sessionStorage
  const staleStorage = remember ? sessionStorage : localStorage

  staleStorage.removeItem(AUTH_STORAGE_KEY)
  targetStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(data))
}

export function getAuth(): LoginData | null {
  const stored = sessionStorage.getItem(AUTH_STORAGE_KEY) ?? localStorage.getItem(AUTH_STORAGE_KEY)

  if (!stored) {
    return null
  }

  try {
    return JSON.parse(stored) as LoginData
  } catch {
    clearAuth()
    return null
  }
}

export function clearAuth() {
  sessionStorage.removeItem(AUTH_STORAGE_KEY)
  localStorage.removeItem(AUTH_STORAGE_KEY)
}

export function updateStoredUser(user: UserInfo) {
  const storage = sessionStorage.getItem(AUTH_STORAGE_KEY) ? sessionStorage : localStorage
  const auth = getAuth()

  if (!auth) {
    return
  }

  storage.setItem(AUTH_STORAGE_KEY, JSON.stringify({ ...auth, user }))
}

export function getDisplayName(user: UserInfo) {
  return user.nickname?.trim() || user.username
}

export function getAvatarText(user: UserInfo) {
  return getDisplayName(user).trim().slice(0, 1).toUpperCase()
}
