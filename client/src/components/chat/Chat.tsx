import { type FC, useState } from "react";
import { useChat } from "@/hooks/useChat";
import { ChatMessages } from "@/components/chat/ChatMessages";
import { ChatButton } from "@/components/chat/ChatButton";
import { ChatHeader } from "@/components/chat/ChatHeader";
import { ChatInput } from "@/components/chat/ChatInput";

export const Chat: FC = () => {
  const [isChatOpened, setIsChatOpened] = useState<boolean>(false);
  const { messages, sendMessage } = useChat();

  return (
    <>
      <ChatButton isChatOpened={isChatOpened} setIsChatOpened={setIsChatOpened} />
      {isChatOpened && (
        <div className="absolute bottom-24 right-6 h-[30rem] w-80 rounded-2xl bg-gray-800">
          <ChatHeader />
          <ChatMessages messages={messages} />
          <ChatInput sendMessage={sendMessage} />
        </div>
      )}
    </>
  );
};
