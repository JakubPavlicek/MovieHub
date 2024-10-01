import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";

export const ChatHeader: FC = () => {
  const { user } = useAuth0();

  return (
    <div className="flex items-center gap-3 rounded-t-2xl bg-gradient-to-r from-cyan-800 to-cyan-600 p-3">
      <img src={user?.picture} alt={user?.nickname} className="size-12 rounded-full border border-cyan-300" />
      <div className="flex flex-col gap-0.5 overflow-hidden">
        <span className="truncate font-medium text-white">{user?.nickname}</span>
        <span className="truncate text-sm text-gray-200">Chat with everyone!</span>
      </div>
    </div>
  );
};
