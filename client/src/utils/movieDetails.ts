export const formatList = (list: { id?: string; name?: string }[] | undefined) =>
  list?.map((item) => item.name).join(", ");

export const transformYouTubeUrl = (url: string): string | undefined => {
  if (!url) return;

  const urlPattern = /(?:https?:\/\/)?(?:www\.)?youtube\.com\/watch\?v=([a-zA-Z0-9_-]+)/;
  const match = urlPattern.exec(url);

  if (!match) return;

  if (match[1]) {
    const videoId = match[1];
    return `https://www.youtube.com/embed/${videoId}`;
  }
};
