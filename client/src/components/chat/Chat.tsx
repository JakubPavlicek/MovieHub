import { type FC, useState } from "react";
import { useChat } from "@/hooks/useChat";
import { ChatMessages } from "@/components/chat/ChatMessages";
import { ChatButton } from "@/components/chat/ChatButton";
import { ChatHeader } from "@/components/chat/ChatHeader";
import { ChatInput } from "@/components/chat/ChatInput";

export const Chat: FC = () => {
  const [isChatOpened, setIsChatOpened] = useState(false);
  const { messages, sendMessage } = useChat();

  console.log(messages);

  return (
    <>
      <ChatButton isChatOpened={isChatOpened} setIsChatOpened={setIsChatOpened} />
      {isChatOpened && (
        <div className="fixed bottom-24 right-8 h-[30rem] w-80 rounded-2xl bg-gray-800 text-white">
          <ChatHeader setIsChatOpened={setIsChatOpened} />
          <ChatMessages messages={messages} />
          <ChatInput sendMessage={sendMessage} />
        </div>
      )}
    </>
  );
};
